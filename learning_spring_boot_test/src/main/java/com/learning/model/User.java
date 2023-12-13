package com.learning.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	
	// remove final if you are not using json property
	private final UUID userUid;
	private final String firstName;
	private final String lastName;
	private final Gender gender;
	private final Integer age;
	private final String email;
	
	public enum Gender{
		MALE,
		FEMALE
	}

	// default constructor
	// using JSON Property
	
	public User(@JsonProperty("userUid")UUID userUid, 
			@JsonProperty("firstName")String firstName, 
			@JsonProperty("userUid")String lastName, 
			@JsonProperty(" gender")Gender gender, 
			@JsonProperty("age")Integer age, 
			@JsonProperty("email")String email) {
		super();
		this.userUid = userUid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.age = age;
		this.email = email;
	}
	
	//used without final variable 
	/*public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	*/
	
	// getter and setter
	// Uncomment setter method if you are using without json property

	@JsonProperty("Id")
	public UUID getUserUid() {
		return userUid;
	}

//	public void setUserUid(UUID userUid) {
//		this.userUid = userUid;
//	}

	public String getFirstName() {
		return firstName;
	}

//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}

	public String getLastName() {
		return lastName;
	}

//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}

	public Gender getGender() {
		return gender;
	}

//	public void setGender(Gender gender) {
//		this.gender = gender;
//	}

	public Integer getAge() {
		return age;
	}

//	public void setAge(Integer age) {
//		this.age = age;
//	}

	public String getEmail() {
		return email;
	}

//	public void setEmail(String email) {
//		this.email = email;
//	}

	// toString Method
	
	
	public static User newUser(UUID userUid, User user) {
		return new User(userUid, user.getFirstName(), user.getLastName(),
				user.getGender(), user.getAge(), user.getEmail());
	}
	@Override
	public String toString() {
		return "User [userUid=" + userUid + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
				+ ", age=" + age + ", email=" + email + "]";
	}

}
