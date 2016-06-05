package com.greenpepper.samples.fixture;

import java.util.Collection;
import com.greenpepper.reflect.CollectionProvider;

public class PhoneBookListWithAnnotationFixture {

	public String firstName;
	public String lastName;

	@CollectionProvider
	public Collection otherName() {
		return null;
	}

}