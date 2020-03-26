# API

- There are 2 http endpoints that accepts JSON base64 encoded binary data on both
endpoints
    - <host>/v1/diff/<ID>/left and <host>/v1/diff/<ID>/right
        - The provided data needs to be diff-ed and the results shall be available on a third end
point
    - <host>/v1/diff/<ID>
        - The results provides the following info in JSON format
            - If equal return that
            - If not of equal size just return that
            - If of same size provide insight in where the diffs are, actual diffs are not needed. 
                - So mainly offsets + length in the data

# Stack used

* Java 8
* Maven
* Spring Boot
* Spock Testing Framework
* H2 DB
* Lombok

# Building

* Compile and package the project

```bash
$ mvn clean package
```

# Running

H2DB is used in the project. It means that you don't need any preparation before start up. When the application is finished, 
in-memory db will not be saved. 
```bash
$ java -jar target/json_diff-0.0.1-SNAPSHOT.jar
```

# Postman

**Json diff.postman_collection.json** is a collection for testing the API. 
Import it to your Postman and use for manual testing.



