package com.app.efficiclean;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class GuestLoginTest {
    @Rule
    public ActivityTestRule<GuestLogin> mActivityRule = new ActivityTestRule(GuestLogin.class);

    @Test
    public void onCreate() throws Exception {
        onView(withId(R.id.etHotelID)).perform(typeText("0582"));
        onView(withId(R.id.etRoomNumber)).perform(typeText("101"));
        onView(withId(R.id.etForename)).perform(typeText("Conor"));
        onView(withId(R.id.etSurname)).perform(typeText("Hanlon"));

        onView(withId(R.id.btLogin)).perform(click());
    }

    @Test
    public void onStart() throws Exception {
    }

    @Test
    public void loginButtonClick() throws Exception {
        GuestLogin guest = mActivityRule.getActivity();

        assertNotEquals(null, guest.hotelID);
        assertNotEquals(null, guest.roomNumber);
        assertNotEquals(null, guest.forename);
        assertNotEquals(null, guest.surname);
    }

    @Test
    public void setValidationValues() throws Exception {
    }

    @Test
    public void validateValues() throws Exception {
    }

}