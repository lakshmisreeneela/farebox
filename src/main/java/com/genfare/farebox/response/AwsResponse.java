package com.genfare.farebox.response;

public class AwsResponse {
	private String environment;

    private String bucket;

    private AwsCredentials credentials;

    public AwsResponse() {

    }

    public AwsResponse(AwsCredentials credentials) {
        this.credentials = credentials;
    }

    public AwsResponse(String bucket, AwsCredentials credentials) {
        this(credentials);
        this.bucket = bucket;
    }

    public AwsResponse(String environment, String bucket, AwsCredentials credentials) {
        this(bucket, credentials);
        this.environment = environment;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public AwsCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(AwsCredentials credentials) {
        this.credentials = credentials;
    }

    public static class AwsCredentials {

        private String accessKey;

        private String secretKey;

        private String sessionId;

        private Long expiration;

        public AwsCredentials() {

        }

        public AwsCredentials(String accessKey, String secretKey, String sessionId, Long expiration) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
            this.sessionId = sessionId;
            this.expiration = expiration;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public Long getExpiration() {
            return expiration;
        }

        public void setExpiration(Long expiration) {
            this.expiration = expiration;
        }

    }
}
