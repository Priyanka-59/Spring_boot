package com.learning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.*;

import com.google.common.collect.ImmutableList;
import com.learning.dao.DummyDataDao;
import com.learning.model.User;
import com.learning.model.User.Gender;
import com.learning.service.UserService;

public class UserServiceTest {

	@Mock
	private DummyDataDao dummyDataDao;
	private UserService userService;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(dummyDataDao);
		userService = new UserService(dummyDataDao);
	}

	@Test
	public void shouldGetAllUsers() throws Exception {
		UUID annaUserUid = UUID.randomUUID();
		User anna = new User(annaUserUid, "anna", "montana", Gender.FEMALE, 30, "anna@example.com");
		dummyDataDao.insertUser(annaUserUid, anna);
		
		//ImmutableList<User> users = new ImmutableList.Builder<User>().add(anna).build();
		//given(dummyDataDao.selectAllUsers()).willReturn(users);
		
		List<User> allUsers = userService.getAllUser(Optional .empty());
		assertThat(allUsers).hasSize(1);
		
		User user = allUsers.get(0);
		assertUserFields(user);
		
		
	}
	
	@Test
	public void shouldGetAllUsersByGender() throws Exception {
		UUID annaUserUid = UUID.randomUUID();
		
		User anna = new User(annaUserUid, "anna", "montana", Gender.FEMALE, 30, "anna@example.com");
		dummyDataDao.insertUser(annaUserUid, anna);
		
		UUID joeUserUid = UUID.randomUUID();
		User joe = new User(joeUserUid, "joe", "jones", Gender.MALE, 30, "joe.jones@example.com");
		dummyDataDao.insertUser(joeUserUid, anna);
		ImmutableList<User> users = new ImmutableList.Builder<User>()
		.add(anna)
		.add(joe)
		.build();
		
		given(dummyDataDao.selectAllUsers()).willReturn(users);
		//List<User> filteredUsers = userService.getAllUser(Optional .of("MALE")); // for male test 
		List<User> filteredUsers = userService.getAllUser(Optional .of("FEMALE"));
		assertThat(filteredUsers).hasSize(1);
		assertUserFields(filteredUsers.get(0));
		
	}
	
	@Test
	public void shouldThrowExceptionWhenGenderIsInvalid() throws Exception{
		assertThatThrownBy(()-> userService.getAllUser(Optional.of("sdsakdsajnd")))
		.isInstanceOf(IllegalStateException.class)
		.hasMessageContaining("Invalid gender");
	}

	@Test
	public void shouldGetUser() throws Exception {
		UUID annaUserUid = UUID.randomUUID();
		User anna = new User(annaUserUid, "anna", "montana", Gender.FEMALE, 30, "anna@example.com");
		dummyDataDao.insertUser(annaUserUid, anna);
		
		//given(dummyDataDao.selectUserByUserUid(annaUserUid)).willReturn(Optional.of(anna));
		
		Optional<User> userOptional = userService.getUser(annaUserUid);
		
		assertThat(userOptional.isPresent()).isTrue();
		User user = userOptional.get();
		
		assertUserFields(user);
	}
	
	
	@Test
	public void shouldUpdateUsers() throws Exception {
		UUID annaUserUid = UUID.randomUUID();
		User anna = new User(annaUserUid, "anna", "montana", Gender.FEMALE, 30, "anna@example.com");
		dummyDataDao.insertUser(annaUserUid, anna);
		
		//given(dummyDataDao.selectUserByUserUid(annaUserUid)).willReturn(Optional.of(anna));
		//given(dummyDataDao.updateUser(anna)).willReturn(1);
		
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		int updateResult = userService.updateUser(anna);
		
		verify(dummyDataDao).selectUserByUserUid(annaUserUid);
		verify(dummyDataDao).updateUser(captor.capture());
		
		User user = captor.getValue();
		assertUserFields(user);
		
		assertThat(updateResult).isEqualTo(1);
		
	}

	@Test
	public void ShouldRemoveUser() throws Exception {
		UUID annaUserUid = UUID.randomUUID();
		User anna = new User(annaUserUid, "anna", "montana", Gender.FEMALE, 30, "anna@example.com");
		dummyDataDao.insertUser(annaUserUid, anna);
		
		//given(dummyDataDao.selectUserByUserUid(annaUserUid)).willReturn(Optional.of(anna));
		//given(dummyDataDao.updateUser(anna)).willReturn(1);
		
		//ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
		int deleteResult = userService.removeUser(annaUserUid);
		
		verify(dummyDataDao).selectUserByUserUid(annaUserUid);
		//verify(dummyDataDao).deleteUserByUserUid(captor.capture());
		verify(dummyDataDao).deleteUserByUserUid(annaUserUid);
		
		//UUID captureAnnaUid = captor.getValue();
		assertThat(deleteResult).isEqualTo(annaUserUid);
		
		assertThat(deleteResult).isEqualTo(1);
		
	}
	
	@Test
	public void shouldInsertUser() throws Exception {
		User anna = new User(null, "anna", "montana", Gender.FEMALE, 30, "anna@example.com");
		//given(dummyDataDao.insertUser(any(UUID.class), eq(anna))).willReturn(1);
		
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		
		int insertResult = userService.insertUser(anna);
		
		verify(dummyDataDao).insertUser(any(UUID.class), captor.capture());
		User user = captor.getValue();
		
		assertUserFields(user);
		
		assertThat(insertResult).isEqualTo(1);
		
	}
	
	
	private void assertUserFields(User user) {
		assertThat(user.getAge()).isEqualTo(30);
		assertThat(user.getFirstName()).isEqualTo("anna");
		assertThat(user.getLastName()).isEqualTo("montana");
		assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
		assertThat(user.getEmail()).isEqualTo("anna.montana@example.com");
		assertThat(user.getUserUid()).isNotNull();
		assertThat(user.getUserUid()).isInstanceOf(UUID.class);
	}


}
