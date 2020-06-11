package com.Instantgram.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String username;
    private String email;
    
    @JsonBackReference
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    
    @JsonBackReference
    private Collection<Role> roles;

	@JsonBackReference
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Post> posts;
	
	
	@JsonBackReference
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "USER_RELATIONS",
        joinColumns = @JoinColumn(name = "FOLLOWED_ID"),
        inverseJoinColumns = @JoinColumn(name = "FOLLOWER_ID"))
    private Set<User> followers;

	@JsonBackReference
	@ManyToMany(mappedBy = "followers")
    private Set<User> following;
	
	
    public User() {
    }

    
    
    public User(String fullName, String username, String email, String password) {
		super();
		this.fullName = fullName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.followers = new HashSet<User>();
        this.following = new HashSet<User>();
	}

    public User(String fullName, String username, String email, String password,
			Set<Post> posts, Set<User> followers, Set<User> following) {
		super();
		this.fullName = fullName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.posts = posts;
		this.followers = followers;
		this.following = following;
	}

    public Collection<Role> getRoles() {
		return roles;
	}



	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String firstName) {
        this.username = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
		return fullName;
	}



	public void setFullName(String fullName) {
		this.fullName = fullName;
	}



	public Set<Post> getPosts() {
		return posts;
	}



	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}



	public Set<User> getFollowers() {
		return followers;
	}



	public void setFollowers(Set<User> followers) {
		this.followers = followers;
	}

	public void addFollower(User follower) {
        followers.add(follower);
        follower.following.add(this);
    }
	
	public void remFollower(User follower) {
        followers.remove(follower);
        follower.following.remove(this);
    }


	public Set<User> getFollowing() {
		return following;
	}



	public void setFollowing(Set<User> following) {
		this.following = following;
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", fullName=" + fullName + ", username=" + username + ", email=" + email
				+ ", password=" + "**********" + ", roles=" + roles + ", posts=" + posts + "]";
	}



}
