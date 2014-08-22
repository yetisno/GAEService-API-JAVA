GAEService-API-JAVA
===================

GAEService API for JAVA

# Install #
Copy src/main/resources/config.json.sample to src/main/resources/config.json

    	Change ADKey's value to your private key in src/main/resources/config.json, which use to do important operation(EX: add user, delete user).

# Usage #
```Java
	GAEService gaes = GAEService.getInstance()
		.setEndPoint("http://myApplicationID.appspot.com/");
	gaes.setToken(gaes.getGAEAdmin().addUser("yeti", "aaa"));
	gaes.setUser(new User("yeti", "aaa"));
	gaes.getKeyValueDatastore().put("abc", "nabc", "aaaaa".getBytes());
	gaes.getKeyValueDatastore().put("abasc", "nabc", "aaaaa".getBytes());
	gaes.getKeyValueDatastore().put("abdc", "nabc", "aaaaa".getBytes());
	gaes.getKeyValueDatastore().put("abac", "nabc", "aaaaa".getBytes());
	System.out.println(gaes.getKeyValueDatastore().get("abc").getKey());
	System.out.println(gaes.getKeyValueDatastore().get("abasc").getKey());
	System.out.println(gaes.getKeyValueDatastore().get("abdc").getKey());
	System.out.println(gaes.getKeyValueDatastore().get("abac").getKey());
	gaes.getKeyValueDatastore().delete("abac");
	gaes.getKeyValueDatastore().delete("abdc");
	gaes.getKeyValueDatastore().delete("abasc");
	gaes.getKeyValueDatastore().delete("abc");
	gaes.getGAEAdmin().deleteUser("yeti");
	gaes.close();
```
