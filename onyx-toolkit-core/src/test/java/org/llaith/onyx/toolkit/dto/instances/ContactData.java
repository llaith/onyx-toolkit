/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto.instances;



import org.llaith.onyx.toolkit.util.reflection.PojoModel.Identity;
import org.llaith.onyx.toolkit.util.reflection.PojoModel.Immutable;
import org.llaith.onyx.toolkit.util.reflection.PojoModel.NestedModel;
import org.llaith.onyx.toolkit.util.reflection.PojoModel.Required;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class ContactData {

    @Identity
    private String id;
    @Immutable
    private String name;
    @Required
    private String email;
    private String phone;
    private boolean contactable;
    private int age;

    @NestedModel
    private ContactData partner;

    @NestedModel
    private final Set<ContactData> relations = new HashSet<>();

    public ContactData() {
        super();
    }

    public ContactData(final String id, final String name, final String email, final String phone,
                       final boolean contactable, final int age, final ContactData partner, final Set<ContactData> relations) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.contactable = contactable;
        this.age = age;
        this.partner = partner;
        if (relations != null) this.relations.addAll(relations);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isContactable() {
        return contactable;
    }

    public int getAge() {
        return age;
    }

    public ContactData getPartner() {
        return partner;
    }

    public Set<ContactData> getRelations() {
        return relations;
    }

    public ContactData setId(final String id) {
        this.id = id;
        return this;
    }

    public ContactData setName(final String name) {
        this.name = name;
        return this;
    }

    public ContactData setEmail(final String email) {
        this.email = email;
        return this;
    }

    public ContactData setPhone(final String phone) {
        this.phone = phone;
        return this;
    }

    public ContactData setContactable(final boolean contactable) {
        this.contactable = contactable;
        return this;
    }

    public ContactData setAge(final int age) {
        this.age = age;
        return this;
    }

    public ContactData setPartner(final ContactData partner) {
        this.partner = partner;
        return this;
    }

    @Override
    public String toString() {
        return "ContactData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", contactable=" + contactable +
                ", age=" + age +
                ", partner=" + partner +
                ", relations=" + relations +
                '}';
    }

}
