# Project 2 : Blogger (Base on Java and RabbitMQ service) 
## Configuration:
### Notes on using installer - installing Erlang
1. First you need to install a [supported version of Erlang](https://www.rabbitmq.com/which-erlang.html) for
Windows.
2. The most recent stable Erlang/OTP release series supported by
RabbitMQ 3.9.x is 24.x.
3. Important : RabbitMQ requires a 64-bit version of Erlang
4. For RabbitMQ 3.9.11 download and run the [Erlang 24.1.7
Windows Installer](https://www.erlang.org/patches/otp-24.1.7).
5. Important : You must run the Erlang installer using an
administrative account otherwise a registry key expected by the
RabbitMQ installer will not be present
### Notes on using installer - installing RabbitMQ
1. download [rabbitmq-server-3.9.11.exe](https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.9.11/rabbitmq-server-3.9.11.exe)
2. Install rabbitmq-server-3.9.11.exe
### Install the RabbitMQ Management Plugin
1. execute command line as administrator
2. change directory to the sbin folder within the RabbitMQ Server
installation directory (e.g. %PROGRAMFILES%\RabbitMQ
Server\rabbitmq_server-3.X\sbin\)
3. next, run the following command to enable the [rabbitmq management
plugin](https://www.rabbitmq.com/management.html):
**_rabbitmq-plugins.bat enable rabbitmq_management_**
4.lastly, to enable the management plugin we need to reinstall the
RabbitMQ service. Execute the following sequence of commands to
reinstall the service:
- rabbitmq-service.bat stop
- rabbitmq-service.bat install
- rabbitmq-service.bat start
5. To verify if the management plugin is up and running, start your
browser and navigate to http://127.0.0.1:15672/
▪ Login (Username: guest | Password: guest) 
### Create a new RabbitMq administrator account
Our app uses a different user for rabbitMq therefore you would need to create using following steps
1) Navigate to 127.0.0.1:15672
2) Login (Username: guest | Password: guest)
3) Under Admin tab press the button Add user
4) Fill user form (note that the field Tags must be administrator)
5) Enter Username: **studentx** | Password: **studentx**
6) Press button Add user
## Run Project
1) Add Java client library, dependencies and Gson library given in project folder
2) Build project and run.