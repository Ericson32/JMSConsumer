package br.com.dummy.jms.bean;

import java.io.Serializable;

public class User implements Serializable {
  
    private long id;
    private String firstName;
    private String surname;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
  
    //Gerar os métodos get/set e toString
}
