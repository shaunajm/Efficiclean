$(document).ready(function () {

	function showPopup(pnum){
	  var docHeight = $(document).height();
	  var scrollTop = $(window).scrollTop();
	  $('.overlay-bg').show().css({'height' : docHeight});
	  $('.popup' + pnum).show().css({'top': scrollTop+20+'px'});
	}

	// function to close our popups
	function closePopup(){
	  $('.overlay-bg, .overlay-content, #loader').hide();
	}

	$('.close-btn, .overlay-bg').click(function(){
	  closePopup();
	});

  function showLoader() {
    var docHeight = $(document).height();
    $('.overlay-bg').show();
    $("#loader").show();
  }

  showLoader();

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

  //Access the values from the query string URL
  const url = window.location.href.split("?");
  const hotelID = url[1].split("=").pop();
  const roomNumber = url[2].split("=").pop();

  //Reference HTML elements
	const logout = document.getElementById("logout-btn");
  const pleaseService = document.getElementById("service-btn");
  const doNotDisturb = document.getElementById("do-not-disturb-btn");
  const checkingOut = document.getElementById("checking-out-btn");

  var status;
  var mRoomRef;
  var uid;

  if (hotelID) {
    mRoomRef = firebase.database().ref().child(hotelID).child("rooms").child(roomNumber).child("status");
    mRoomRef.on("value", snap => {
      if (snap.val()) {
        status = snap.val();
        if (status == "Idle") {
          $("#status").text("Please select your room's status.");
        } else if (status == "Do Not Disturb") {
          $("#status").text("Current room status: Do not Disturb");
        } else if (status == "Completed") {
          $("#status").text("Current room status: Serviced");
        } else {
          $("#status").text("Current room status: In Process");
        }
      }
      closePopup();
    });
  }

  pleaseService.addEventListener("click", e => {
    if (status == "Completed") {
      showPopup(1);
    } else if (status != "Idle" && status != "Do Not Disturb") {
      showPopup(2);
    } else {
      mRoomRef.set("To Be Cleaned");
      firebase.database().ref().child(hotelID).child("jobs").push().set({
        createdBy: uid,
        priority: 0,
        roomNumber: roomNumber.toString(),
        status: false
      });
      showPopup(4);
    }
  });

  doNotDisturb.addEventListener("click", e => {
    if (status == "Completed") {
      showPopup(1);
    } else if (status != "Idle" && status != "Do Not Disturb") {
      showPopup(2);
    } else {
      mRoomRef.set("Do Not Disturb");
      showPopup(3);
    }
  });

  checkingOut.addEventListener("click", e => {
    if (status == "Completed") {
      showPopup(1);
    } else if (status != "Idle" && status != "Do Not Disturb") {
      showPopup(2);
    } else {
      mRoomRef.set("To Be Cleaned");
      firebase.database().ref().child(hotelID).child("jobs").push().set({
        priority: 0,
        roomNumber: roomNumber.toString(),
        status: false
      });
      showPopup(5);
    }
  });

  //Event listener to perform logout operation
  logout.addEventListener("click", e => {
    firebase.auth().signOut().then(() => {
      console.log("User signed out.");
    }, e => {
      console.log("Error signing out: ", e);
    });
  });

  //Listens to the Firebase authentication
  firebase.auth().onAuthStateChanged(firebaseUser => {
    if(firebaseUser) {
      console.log(firebaseUser);
      uid = firebaseUser.uid;
    } else {
      console.log("No user currently logged in.");
      history.back();
    }
  });
});