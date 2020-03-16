create database securitydemo;
create user 'sec@%' identified by 'sec@123';
grant all privileges on securitydemo.* to sec;
