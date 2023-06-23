In this project, the email,password, authority(string) of user is stored in postgres. Only a user having authority "ROLE_ADMIN" can delete a user from database.
I created a custom class named AuthorityCollection to convert the authority string to collection.
I am using a cookie to track the user who has logged in .