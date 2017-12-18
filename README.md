Installation Setup
---
 1. Install and configure Maven 3.5 : 
    https://maven.apache.org
      
 2. Install and configure JDK 8 : 
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html  

 3. Install MySQL then create a database named `hadithdb`.
 
 4. Configure the application to access the database created:
    - In `src/resources/hibernate.cfg.xml` file, 
    configure the fields `url`, `username` and `password`. 
    - In `src/main/java/ontology/Config.java` file, 
    configure the fields `DB_URL`, `DB_USER` and `DB_PASSWD`. 
    
  5. Configure Indexes folder value to an accessible one : 
      - In `src/resources/hibernate.cfg.xml` file,                            
   ```xml
   <property name="hibernate.search.default.indexBase">d:\indexes</property>  
   ```
        