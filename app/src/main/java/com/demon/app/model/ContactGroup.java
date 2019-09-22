package com.demon.app.model;

import java.util.ArrayList;

import androidx.annotation.NonNull;

/**
 * @Author create by Zoran on 2019-09-20
 * @Email : 171905184@qq.com
 * @Description :
 */
public class ContactGroup {

	private String title;

	private ArrayList<Contact> contacts = new ArrayList<>();

	public ContactGroup(@NonNull String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public ArrayList<Contact> getContacts() {
		return this.contacts;
	}

	public void addContact(Contact contact) {
		this.contacts.add(contact);
	}

	public void addContact(int index, Contact contact) {
		this.contacts.add(index, contact);
	}

	public int size() {
		return this.contacts.size();
	}
}
