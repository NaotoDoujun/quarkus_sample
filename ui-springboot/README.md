# ui-springboot

Authorization in Spring Boot with Auth0 SAML

## Auth0 Settings

Allowed URLs are like this.  
![Allowed URLs](images/auth0_1.png "auth0_1.png")

Use SAML addon.  
![SAML addon](images/auth0_2.png "auth0_2.png")

Callback URL is like this.   
![Callback URL](images/auth0_3.png "auth0_3.png")

Added test user.  Password is P@ssw0rd   
![User](images/auth0_4.png "auth0_4.png")

## Run
```
./mvnw spring-boot:run
```
Logs are like this.  
![logs](images/logs.png "logs.png")

Auth screen.  
![Auth Screen](images/auth_screen.png "auth_screen.png")

After Signed in, You can see Principal one home.  
![Home Screen](images/home_screen.png "home_screen.png")