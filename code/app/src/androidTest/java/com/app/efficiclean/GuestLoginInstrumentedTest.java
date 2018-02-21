package com.app.efficiclean;

import android.support.test.rule.ActivityTestRule;
import com.app.efficiclean.activities.GuestLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GuestLoginInstrumentedTest {

    @Rule
    public ActivityTestRule<GuestLogin> mActivity = new ActivityTestRule<GuestLogin>(GuestLogin.class);

    private GuestLogin gLogin;

    @Before
    public void setUp() throws Exception {
        gLogin = mActivity.getActivity();
    }

    @Test
    public void testStart() {
        assertNotNull(gLogin.hotelID);
        assertNotNull(gLogin.roomNumber);
        assertNotNull(gLogin.forename);
        assertNotNull(gLogin.surname);
        assertNotNull(gLogin.spinner);
        assertNotNull(gLogin.loginBtn);
        assertNotNull(gLogin.mRootRef);
        assertNotNull(gLogin.mAuth);
        assertNotNull(gLogin.mAuthListener);
    }

    @Test
    public void loginButtonClick() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        gLogin = null;
    }

}