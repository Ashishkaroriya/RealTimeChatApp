# RealTime Chat Application (RTCA)

**Built with Java WebSocket,Maven and Tomcat on real time chat application with some dynamic  frontend properties**


## Features

- Streamlined messaging through WebSocket
Real-time chat functionality supporting Multi room( user-defined chat room)
- User authentication for secure access
Dynamic responsive UI wth theme-switching
- Chat History is maintained storing the latest 100 messages per room
System commands enhance usability
/ login <username>: you are to log in with a distinct username.
/ join <roomName> : when you join or create chat room.
/ rooms : view all rooms
/name <newName>Hello! Change your display name.
leave : leave the chat room where you are.


## Employed Technologies

backend — Java WebSocket API,Apache Maven,Apache Tomcat
Frontend : HTML5, CSS3,JavaScript
** Version control: As Git for track the folder issue


## Running the Project

Installing & Running the chat application is very simple as necessary to do this:


### **Step 1: Clone the Repository
1. Open a terminal or command prompt.
* Init clone from GitHub repository
* git clone https://github.com//RealTimeChatApp.git
2. Go to the project directory:
cd RealTimeChatApp


Step 2 : Build the Project
1. First install maven in your system.
2. The following command is used to clean and build :
``` bash
3. mvn clean install
```
4. A `.war` file (your folder: `RTCA.war`) will be generated in the target directory.

### **3: ** Deploy the Application with Apache Tomcat
1. Install Apache Tomcat (at least version 7).
2. Copy the `.war` file you generate(`target/RTCA.war`) to tomcat webapps directory.
3. Start Tomcat:
   - Windows: Use `startup.bat` from the Tomcat `bin` directory.
   - Linux/Mac: Use `catalina.sh start` from the Tomcat `bin` folder.
4. Next, go to a web browser with:
   ```text
   http://localhost:8989
   ```


### **Step 4: Run the Application Using IntelliJ IDEA
1. Open Idea
2. Select File > Open and offer the project folder.
3. import the project in maven (IntelliJ will identify the `pom.xml`)
4. Configure Tomcat server:
- Go to **Run > Edit Configurations**
- Add a new **Tomcat Server > Local** configuration.
- Install Tomcat Installation directory
- Deploy the `RTCA:war` artifact under the tab as shown rendered in Dashboard.
5. Click **Run** button and access your application at
   ```text
   http://localhost:8989
   ```


### **Step 5 : Run with Embedded Tomcat**
You can execute a application directly from the `embedded tomcat` plugins been configured in the `pom.xml`:
1. Go to the project root directory and open a terminal there.
2. Execute Maven command :
   ```bash
   mvn tomcat7:run
   ```
3. Open a web browser at
   ```text
   http://localhost:8989
   ```

## Additional Notes

- **Port**: Make sure that the port you defined in `pom.xml` or tomcat (8989 in below example) is not filtered or used by some other process.
***Firewall – *** Run on remote server? Then also, turn on your firewall and allow connections to the configured port



## Contributing

Contribution Welcome! To contribute:
1. Fork the Repository.
2. Create a branch for your changes:
   ```bash
    git checkout -b <name-of-your-bug-added-branch>
   ```
3. Make changes:
   ```bash
   git commit -m 'Add feature description'
   ```
4. Push your branch to your fork:
   ```bash
   git push origin feature-name
   ```
5. Submit a Pull Request



## License

This project is released under the MIT License



## Contact

Questions or Ideas you may have email Me —>
 **Author**: Ashish
**Email –** ashishkaroria1@gmail.com 
 
