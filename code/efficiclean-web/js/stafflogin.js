$(document).ready(function () {

  function showPopup(){
    var docHeight = $(document).height();
    var scrollTop = $(window).scrollTop();
    $('.overlay-bg').show().css({'height' : docHeight});
    $('.popup').show().css({'top': scrollTop+20+'px'});
  }
  // function to close our popups
  function closePopup(){
    $('.overlay-bg, .overlay-content').hide();
  }

  $('.close-btn, .overlay-bg').click(function(){
    closePopup();
  });

  //Firebase configuration
  const config = {
    apiKey: "AIzaSyDVb3XPQ2Y8TIozdgY11705R1IZP6ygw70",
    authDomain: "efficiclean.firebaseapp.com",
    databaseURL: "https://efficiclean.firebaseio.com",
    projectId: "efficiclean",
    storageBucket: "efficiclean.appspot.com",
    messagingSenderId: "574703653724"
  };
  firebase.initializeApp(config);

  if (firebase.auth().currentUser) {
    firebase.auth().signOut();
  }

  //Reference the html objects
  const hid = document.getElementById("hid");
  const uname = document.getElementById("uname");
  const pword = document.getElementById("pword");
  const login = document.getElementById("login-btn");

  //Add event listener to login button
  login.addEventListener("click", e => {
    //Reference values inputted by user
    var hotelID = hid.value;
    var username = uname.value;
    var password = pword.value;

    //Authentication status
    const auth = firebase.auth();

    if (hotelID && username && password) {
      //Reference to the staff branch in the database
      const mStaffRef = firebase.database().ref().child(hotelID).child("staff");

      //Add value event listener to the database reference
      mStaffRef.on("value", snap => {
        var isValid = false;

        //loops through each child in the branch
        snap.forEach(housekeeper => {
          //Validates that the a child has the inputted username and password
          if (housekeeper.child("username").val() == username &&
            housekeeper.child("password").val() == password) {
            //Logs in the user
            var promise = auth.signInWithEmailAndPassword(username + "@efficiclean.com", password);
            promise.catch(e => console.log(e.message));

            isValid = true;
          }
        });

        //Displays message if details are incorrect
        if (isValid == false) {
          showPopup();
        }
      });
    } else {
      showPopup();
    }
    
  });

  //Listens to the Firebase authentication
  firebase.auth().onAuthStateChanged(firebaseUser => {
    if(firebaseUser) {
      //Redirects to map page
      console.log(firebaseUser);
      window.location.href = "mapview.html?hid=" + hid.value;
    } else {
      console.log("No user currently logged in.");
    }
  });

});