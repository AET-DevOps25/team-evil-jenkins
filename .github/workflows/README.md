# CI/CD Pipeline Documentation

This document outlines the Continuous Integration (CI) and Continuous Deployment (CD) pipelines configured for this project. These pipelines automate the processes of building, testing, and deploying the microservices and frontend application, ensuring a streamlined and reliable delivery process.

## Build Docker Images Workflow (`build_docker.yml`)

This workflow is responsible for building and testing all services (Java microservices, Python GenAI service, React frontend) and then packaging them into Docker images. It is triggered on every `push` to the repository and for every `pull_request` targeting the `main` branch.

### Jobs:

#### 1. `changes` (Detect File Changes)

* **Purpose**: This job detects if there are any changes within the `client/` directory. This output can be used by subsequent jobs to conditionally run steps related to the frontend.

* **Steps**:

  * Checks out the repository.

  * Uses `tj-actions/changed-files@v44` to identify changes in the `client/**` path.

#### 2. `java-build` (Build Java Microservices)

* **Purpose**: This job builds, tests, and packages the Java-based microservices. It runs in parallel for each specified service.

* **Strategy**: Uses a matrix strategy to build `userservice`, `locationservice`, `matchingservice`, and `messagingservice` concurrently.

* **Steps**:

  * Checks out the repository.

  * Sets up Java Development Kit (JDK) version 21 using Temurin distribution.

  * Sets up Gradle.

  * **Get Service Version**: Executes a Gradle task to extract the service's version and stores it as a GitHub output and in a `version.txt` file.

  * **Build with Gradle**: Compiles and builds the service using Gradle.

  * **Run Tests with Gradle**: Executes unit and integration tests for the service.

  * **Upload Service Version**: Uploads the `version.txt` file as an artifact.

  * **Upload Service Artifact**: Uploads the built `.jar` file as an artifact.

#### 3. `genai-python-test` (Test GenAI Python Service)

* **Purpose**: This job is dedicated to setting up the Python environment, installing dependencies, and running tests for the GenAI Python service.

* **Steps**:

  * Checks out the repository.

  * Sets up Python version 3.11.

  * Installs Python dependencies from `genai/requirements.txt`.

  * Runs `pytest` for the GenAI service.

#### 4. `build-react-frontend` (Build React Frontend)

* **Purpose**: This job builds the React single-page application (SPA) frontend. It is designed to be efficient by leveraging caching for npm dependencies, which significantly speeds up subsequent builds when changes are concentrated within the client/ directory.

* **Steps**:

  * Checks out the code.

  * Sets up Node.js version 18 and caches npm dependencies using cache-dependency-path: ./client/package-lock.json. This ensures that dependencies are reused across runs, improving pipeline performance.

  * Installs Node.js dependencies.

  * **Build React app**: Executes the `npm run build` command within the `client` directory to create the production build. Environment variables for Auth0 configuration are passed during the build.

  * **Upload React Build Artifact**: Uploads the `dist` directory (containing the built React app) as an artifact.

#### 5. `publish-client-image` (Build and Publish Client Docker Image)

* **Purpose**: This job builds a Docker image for the React frontend and publishes it to the GitHub Container Registry (GHCR). It runs only if the `build-react-frontend` job was successful.

* **Dependencies**: Requires `build-react-frontend`.

* **Steps**:

  * Checks out the repository.

  * Downloads the `react-client-build-artifact` generated by the `build-react-frontend` job.

  * Logs in to GHCR using `github.actor` and `GITHUB_TOKEN`.

  * Sets up QEMU for multi-architecture builds.

  * Sets up Docker Buildx.

  * **Extract metadata**: Generates Docker image tags and labels based on Git information (short SHA, branch name, PR number, `latest` for default branch).

  * **Build and push Docker Image**: Builds the Docker image from the `client/Dockerfile` for `linux/amd64` and `linux/arm64` platforms and pushes it to GHCR.

#### 6. `publish-docker-images` (Build and Publish Docker Images for Services)

* **Purpose**: This job builds Docker images for all backend services (Java, Python, Nginx) and publishes them to GHCR.

* **Dependencies**: Requires `java-build` and `genai-python-test` to ensure all necessary build artifacts are available.

* **Strategy**: Uses a matrix strategy to build images for `location-service`, `user-service`, `matching-service`, `genai`, `messaging-service`, and `nginx-gateway`.

* **Steps**:

  * Checks out the repository.

  * **Download Version artifact**: Downloads the version artifact for Java services (if applicable).

  * **Read Version from file**: Reads the service version from the downloaded `version.txt` file.

  * **Download JAR artifact**: Downloads the built JAR file for Java services.

  * **Download React Build Artifact**: (Conditional) Downloads the React build artifact if the service configuration indicates it's a client (though this is primarily handled by `publish-client-image`).

  * Logs in to GHCR.

  * Sets up QEMU for multi-architecture builds.

  * Installs Docker Buildx.

  * **Extract metadata**: Generates Docker image tags and labels, incorporating the Java service version if available.

  * **Build and push Docker Image**: Builds the Docker image from the respective `Dockerfile` for `linux/amd64` and `linux/arm64` platforms and pushes it to GHCR. Includes caching for the `nginx-gateway` image.

## Deploy Manually to Kubernetes via Helm Workflow (`deploy_helm.yml`)

This workflow enables manual deployment of the application to a Kubernetes cluster using Helm. It is designed for controlled deployments to the `production` environment.

### Trigger:

* **`workflow_dispatch`**: push: This workflow is triggered on pushes to the main branch.

### Jobs:

#### 1. `deploy`

* **Purpose**: This job handles the deployment of the application to the Kubernetes cluster.

* **Environment**: Runs in the `production` environment, implying that environment-specific secrets and variables are used.

* **Steps**:

  * **Checkout repository**: Retrieves the repository code.

  * **Set up kubectl**: Installs the `kubectl` command-line tool.

  * **Write kubeconfig from secret**: Decodes the base64-encoded Kubernetes configuration from a GitHub secret (`KUBECONFIG_DATA`) and writes it to a file, then sets it as the `KUBECONFIG` environment variable. This allows `kubectl` to authenticate with the cluster.

  * **Set up Helm**: Installs the Helm package manager.

  * **Create/Update Auth0 K8s Secret**: Creates or updates a Kubernetes generic secret named `auth0-secrets` in the `team-evil-jenkins` namespace. This secret contains sensitive Auth0 configuration details, sourced from GitHub secrets and variables.

  * **Create/Update GenAI K8s Secret**: Creates or updates a Kubernetes generic secret named `genai-secrets` in the `team-evil-jenkins` namespace. This secret holds configuration for the GenAI service, also sourced from GitHub secrets and variables.

  * **Helm upgrade/install**: Executes the Helm command to either install a new release or upgrade an existing one for the `team-evil-jenkins` chart. It targets the `team-evil-jenkins` namespace and includes a `redeploy` flag with a timestamp to force a redeployment if needed.