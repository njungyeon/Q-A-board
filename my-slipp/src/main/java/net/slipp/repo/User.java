package net.slipp.repo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class User extends AbstractEntity {
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@JsonProperty
//	private Long id;
	
	@JsonProperty
	private String name;
	
	
	@Column(nullable=false, length=20, unique=true)
	@JsonProperty
	private String userId;
	
	@JsonIgnore
	private String password;
	
	@JsonProperty
	private String email;

//	public Long getId() {
//		return id;
//	}
	
	public boolean matchId(Long newId) {
		if(newId == null) {
			return false;
		}
		return newId.equals(getId());
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean matchPassword(String newPassword) {
//		if(this.password != newPassword) {
//			return false;
//		}
//		return true;
		if(newPassword == null) {
			return false;
		}
		return newPassword.equals(password);
		//이 부분은 this로 선언 안해도 접근이 가능한건가
		
	}
	
//	public String getPassword() {
//		return password;
//	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		User other = (User) obj;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		return true;
//	}

	@Override
	public String toString() {
		return "User [ id=" + getId() + "name=" + name + ", userId=" + userId + ", password=" + password + ", email=" + email + "]";
	}

	public void update(User newUser) {
		this.password = newUser.password;
		this.email = newUser.email;
		this.name = newUser.name;
	}

 }
