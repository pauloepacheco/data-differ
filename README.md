# Data Differ API

This is a REST API tool that provides the comparison between two Base64 binary data. 

The provided data needs to be diff-ed and the results shall be available thru the /v1/diff/{id} API. 

The results shall provide the following info in JSON format

	o If equal return that  
	o If not of equal size just return that
	o If of same size provide insight in where the diffs are, actual diffs are not needed.
		§ So mainly offsets + length in the data

### List of supported endpoints
  
| Method  | Content-Type            | URI                 |
| ------------- | -------------     | ---------------     |
| PUT           | application/json  | /v1/diff/{id}/left  |
| PUT           | application/json  | /v1/diff/{id}/right |
| GET           |                   | /v1/diff/{id}       |

### List of supported HTTP status code for the Data Differ API:

| HTTP Code | Description                                   |
| ----------| --------------------------------------        |
| 200       | The request has successfully processed        |
| 201       | The request has successfully created/updated  |
| 400       | Bad Request                                   |
| 500       | An internal error has occurred                |
		
## Usage
	mvn-boot:run - Starts the servless application and uses an embedded Tomcat container to expose the REST API's
	mvn package  - Creates a fat jar to be executed
	mvn test     - Runs all the written test cases
  
### Samples Request

  Sending a request to the **left** endpoint
  
      curl -i -H "Content-Type: application/json" -X PUT -d '{"encoded_data":"cGF1bG8"}' http://<host>/v1/diff/{id}/left
  
  Sending a request to the **right** endpoint
  
      curl -i -H "Content-Type: application/json" -X PUT -d '{"encoded_data":"cGF1bG8"}' http://<host>/v1/diff/{id}/left
      
  Sending a request to the **diff** endpoint for comparison
  
      curl -X GET -i <host>/v1/diff/{id}
      
### Samples Response

    Sample of a validation response payload
```json      
    {
      "error_category": "Validation Error",
      "errors":[
        "The provided ID does not exist"
      ]
    }
```
  Sample of an invalid Payload
```json  
    { 
      "error_category": "Validation Error",
      "errors":[
        "encoded_data is mandatory"
      ]
    }
```
  Sample of a content does not match comparison
```json  
  {
    "message": "CONTENT_DOES_NOT_MATCH",
    "result":[
      {
        "offset": 6,
        "length": 3
      },
      {
        "offset": 3,
        "length": 2
      }
    ]
  }
```
## Stack
  **Java 8**
  
  **Hibernate**
  
  **Spring Framework**
  
  **Maven**
  
  **H2 Database**
  
  **JUnit**
  
  **Mockito**
							
