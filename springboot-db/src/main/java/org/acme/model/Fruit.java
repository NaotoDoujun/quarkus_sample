package org.acme.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fruits")
public class Fruit {
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

    @Column(name = "name")
    private String name;

    public Fruit() {
    }

    public Fruit(String name) {
        this.name = name;
    }

    public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @Override
	public String toString() {
		return "Fruit [id=" + id + ", name=" + name + "]";
	}

}