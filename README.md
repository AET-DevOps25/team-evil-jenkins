## Problem statement: 

### Main Functionality

The core functionality of the application is to connect individuals with shared interests in outdoor sports and physical activities. Users will be able to create personalized profiles, specify their preferred sports and availability, and discover potential partners or groups in their area. The platform will facilitate interaction through features like recommendations, matching, and communication tools. The goal is to reduce the time and effort typically required to find suitable activity partners, while offering a user-friendly and engaging experience.


### Intended Users

The intended users can be any person who is interested in doing sports on every possible level for example:
* Amateur outdoor sport enthusiasts looking for training partners.
* Newcomers to a city who want to stay active and meet others.
* Busy professionals with limited time who want easy connections.
* Groups or communities planning local sports events and looking for attendees.



### GenAI Integration

GenAI will be used to enhance the user matching experience by analyzing user profiles in a more intelligent and context-aware manner. Instead of relying solely on standard filters like location or shared interests, GenAI can predict compatibility with a potential partner by interpreting patterns in user data.

### Possible Scenarios

* Scenario 1: New to City
Lena moves to Munich and wants to find a group to go hiking. She signs up, selects "hiking" as a favorite activity, and adds her availability. By clicking "Show matches" button, the web platform shows matching users. Lena can afterwards get in touch with the recommended users. 

* Scenario 2: Busy Professional
Max is a busy professional who wants to stay active but doesn't have time to organize his own events. He signs up, fills the post form (e.g. selects all the sports he would like to do with someone together, and adds his availability and other information). The web platform lists matching users. Max can afterwards get in touch with the recommended users.

* Scenario 3: Preparing for Marathon
Ali is training for a half marathon and wants running partners for early mornings. He signs up, selects "running" as a favorite activity, and adds his availability and other preferences. By clicking "Show matches" button, the web platform lists matching users. Ali can afterwards get in touch with the recommended users.


### Technical Description 

##  Architecture Overview


* Server (Spring Boot REST API): Manages user profiles, matching, and communication via REST endpoints. Built with Spring Boot, it handles CRUD operations, authentication, and orchestrates interactions.
* Client (React Frontend): Provides a user-friendly interface for profile creation, match viewing, and messaging. 
* GenAI Service (Python LangChain): Enhances matching by predicting compatibility using LangChain and processes user data with LLM embedding models.
* Database (PostgreSQL): Stores users profiles, sports, matches, and messages.

