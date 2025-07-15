{{/*
Expand the name of the chart.
*/}}
{{- define "monitoring-chart.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "monitoring-chart.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart-wide standard labels.
*/}}
{{- define "monitoring-chart.labels" -}}
helm.sh/chart: {{ include "monitoring-chart.name" . }}-{{ .Chart.Version | replace "+" "_" }}
{{ include "monitoring-chart.selectorLabels" . }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "monitoring-chart.selectorLabels" -}}
app.kubernetes.io/name: {{ include "monitoring-chart.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}
