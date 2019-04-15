# CYBR8480Project
## Executive Project Summary-
 In this project I am planning to develop an android app which collects data from motion sensors, position sensors, and  environment sensors.  The idea is to collect all the possible data available from device sensors store them on elastic search with timestamps. The data will be later used to train and predict user behavior by applying machine learning techniques such as random forest using Tensor Flow.
### Goals :- 
* Collect data from motion sensors, position sensors, and  environment sensors
* Store the collected data in Elastic Search
* Apply Machine Learning using Tensor Flow to predict or observe any pattern from the collected data 

### Merit of the Project:- 
 * Project aims to provide an idea about the information that can be collected from Android Sensors. 
### Application requirements 
## User stories
* As a user, I can download the app
* As a user, I allow which information from the sensors can be shared with the app
* As a user, I can access Elastic search and see the information which has been sent from my device
* As a user I can login to the website and see the machine learning output from the training set
* As a user I can access only the information sahred by my device.

## Misuser stories
* As a misuser, I want to access information not shared by my device
* As a misuser, I want to access the machine learning output of training data not shared by my device
* As a misuser, I want to access all the data collected in elastic search without proper authorization

## High Level Design
Context Diagram :- Context Diagram.png
Container Diagram :- container.pdf
Component Diagram:- Component.pdf

## Data Collection from Sensor
### 
This component will collect data from the device. The app will be running in background and sending information to the service every 1 hour. 
### Elastic Search
This component will store all the information collected from the sensor.

### Information Viewing system
This will be a web layout which will allow users to view their information which was collected from their device sensor.


### Security analysis
| Component name | Category of vulnerability | Issue Description | Mitigation |
|----------------|---------------------------|-------------------|------------|
|Data Collection from Sensor|Data Theft | Application shouldnt allow any third party to collect the data collected from the sensor | App only stores the information and sends it as a batch to ES, stores in local database using Room |
|Elastic search |Data Theft| Elastic Search data is stored in HCC cloud data can be stolen or misused  |Elastic Search viewing system is secured and all request will be processed via security token  |
