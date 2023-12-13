package com.learning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.learning.dao.DummyDataDao;
import com.learning.model.User;
import com.learning.model.User.Gender;

public class DummyDataDaoTest {

private DummyDataDao dummyDataDao;
	
	@Before
	public void setUp() throws Exception{
		dummyDataDao = new DummyDataDao();
	}
	
	@Test
	public void shouldSelectAllUsers() throws Exception{
		List<User> users = dummyDataDao.selectAllUsers();
		assertThat(users).hasSize(1);
		User user = users.get(0);
		assertThat(user.getAge()).isEqualTo(22);
		assertThat(user.getFirstName()).isEqualTo("Joe");
		assertThat(user.getLastName()).isEqualTo("Jones");
		assertThat(user.getGender()).isEqualTo(Gender.MALE);
		assertThat(user.getEmail()).isEqualTo("joe.jones@example.com");
		assertThat(user.getUserUid()).isNotNull();
		
	}
	
	@Test
	public void shouldSelectUserByUserUid() throws Exception{
		UUID annaUserUid = UUID.randomUUID();
		User anna = new User(annaUserUid, "anna", "montana", Gender.FEMALE, 30, "anna@example.com");
		dummyDataDao.insertUser(annaUserUid, anna);
		assertThat(dummyDataDao.selectAllUsers()).hasSize(2);
		Optional<User> annaOptional = dummyDataDao.selectUserByUserUid(annaUserUid);
		assertThat(annaOptional.isPresent()).isTrue();
		assertThat(annaOptional.get()).isEqualToComparingFieldByField(anna);
	}
	
	@Test
	public void shouldSelectUserByRandomUserUid() throws Exception{
		Optional<User> user = dummyDataDao.selectUserByUserUid(UUID.randomUUID());
		assertThat(user.isPresent()).isTrue();
	}
	
	@Test
	public void shouldUpdateUser() throws Exception{
		UUID joeUserUid = dummyDataDao.selectAllUsers().get(0).getUserUid();
		User newJoe = new User(joeUserUid, "joe", "jones", Gender.MALE, 22, "joe.jones@example.com");
		dummyDataDao.updateUser(newJoe);
		Optional<User> user = dummyDataDao.selectUserByUserUid(joeUserUid);
		assertThat(user.isPresent()).isTrue();
		
		assertThat(dummyDataDao.selectAllUsers()).hasSize(1);
		assertThat(user.get()).isEqualToComparingFieldByField(newJoe);
	}
	
	@Test
	public void shouldDeleteUserByUserUid() throws Exception{
		UUID joeUserUid = dummyDataDao.selectAllUsers().get(0).getUserUid();
		
		dummyDataDao.deleteUserByUserUid(joeUserUid);
		
		assertThat(dummyDataDao.selectUserByUserUid(joeUserUid).isPresent()).isFalse();
		assertThat(dummyDataDao.selectAllUsers()).isEmpty();
		
	}
	
	@Test
	public void shouldInsertUser() throws Exception{
		UUID userUid = UUID.randomUUID();
		User user = new User(UUID.randomUUID(), "anna", "montana", Gender.FEMALE, 30, "anna@example.com");
		
		dummyDataDao.insertUser(userUid, user);
		
		List<User> users = dummyDataDao.selectAllUsers();
		assertThat(users).hasSize(2);
		assertThat(dummyDataDao.selectUserByUserUid(userUid).get()).isEqualToComparingFieldByField(users);
	}
}
