package com.greenpepper.samples.fixture;

import com.greenpepper.reflect.EnterRow;

public class PhoneBookSetupWithAnnotationFixture {

    public String firstName;
    public String lastName;

    @EnterRow
    public void otherName() { }
}