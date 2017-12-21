package com.genfare.farebox.security;

import java.util.List;

public class SecurityTokenResponse {
	
	private AWS aws;
    private UriMap uriMap;

    public SecurityTokenResponse() {
    }

    public SecurityTokenResponse(AWS aws, UriMap uriMap) {
        this.aws = aws;
        this.uriMap = uriMap;
    }

    public UriMap getUriMap() {
        return uriMap;
    }

    public void setUriMap(UriMap uriMap) {
        this.uriMap = uriMap;
    }

    public AWS getAws() {
        return aws;
    }

    public void setAws(AWS aws) {
        this.aws = aws;
    }

    public static class AWS {
        private String configurationBucket;
        private String environment;
        private Credentials credentials = new Credentials();

        public AWS() {
        }

        public AWS(String configurationBucket, String environment, Credentials credentials) {
            this.configurationBucket = configurationBucket;
            this.environment = environment;
            this.credentials = credentials;
        }

        public String getConfigurationBucket() {
            return configurationBucket;
        }

        public void setConfigurationBucket(String configurationBucket) {
            this.configurationBucket = configurationBucket;
        }

        public String getEnvironment() {
            return environment;
        }

        public void setEnvironment(String environment) {
            this.environment = environment;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }

        public static class Credentials {
            private String accessKeyId;
            private String secretAccessKey;
            private String sessionToken;
            private long expiration;

            public Credentials() {
            }

            public Credentials(String accessKeyId, String secretAccessKey, String sessionToken, long expiration) {
                this.accessKeyId = accessKeyId;
                this.secretAccessKey = secretAccessKey;
                this.sessionToken = sessionToken;
                this.expiration = expiration;
            }

            public String getAccessKeyId() {
                return accessKeyId;
            }

            public void setAccessKeyId(String accessKeyId) {
                this.accessKeyId = accessKeyId;
            }

            public String getSecretAccessKey() {
                return secretAccessKey;
            }

            public void setSecretAccessKey(String secretAccessKey) {
                this.secretAccessKey = secretAccessKey;
            }

            public String getSessionToken() {
                return sessionToken;
            }

            public void setSessionToken(String sessionToken) {
                this.sessionToken = sessionToken;
            }

            public long getExpiration() {
                return expiration;
            }

            public void setExpiration(long expiration) {
                this.expiration = expiration;
            }

        }

    }

    public static class UriMap {
        private Services services;
        private List<Configuration> configurations;

        public UriMap() {
        }

        public UriMap(Services services, List<Configuration> configurations) {
            this.services = services;
            this.configurations = configurations;
        }

        public Services getServices() {
            return services;
        }

        public void setServices(Services services) {
            this.services = services;
        }

        public List<Configuration> getConfigurations() {
            return configurations;
        }

        public void setConfigurations(List<Configuration> configurations) {
            this.configurations = configurations;
        }

        public static class Services {
            private Service event;
            private Service setup;

            public Services() {
            }

            public Services(Service event, Service setup) {
                this.event = event;
                this.setup = setup;
            }

            public Service getSetup() {
                return setup;
            }

            public void setSetup(Service setup) {
                this.setup = setup;
            }

            public Service getEvent() {
                return event;
            }

            public void setEvent(Service event) {
                this.event = event;
            }

            public static class Service {
                private String link;
                private String method;

                public Service() {
                }

                public Service(String link, String method) {
                    this.link = link;
                    this.method = method;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getMethod() {
                    return method;
                }

                public void setMethod(String method) {
                    this.method = method;
                }

            }
        }

        public static class Configuration {
            private String link;
            private String method;
            private String key;

            public Configuration() {
            }

            public Configuration(String link, String method, String key) {
                this.link = link;
                this.method = method;
                this.key = key;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getMethod() {
                return method;
            }

            public void setMethod(String method) {
                this.method = method;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

        }

    }


}
