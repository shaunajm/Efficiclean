$(document).ready(function () {

	function showPopup(pnum){
	  var docHeight = $(document).height();
	  var scrollTop = $(window).scrollTop();
	  $('.overlay-bg').show().css({'height' : docHeight});
	  $('.popup' + pnum).show().css({'top': scrollTop+20+'px'});
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

  	//Reference HTML elements
	const hid = document.getElementById("hid");
	const rnum = document.getElementById("rnum");
	const fname = document.getElementById("fname");
	const sname = document.getElementById("sname");
	const login = document.getElementById("login-btn");

	login.addEventListener("click", e => {
		var hotelID = hid.value;
		var roomNumber = rnum.value;
		var forename = fname.value;
		var surname = sname.value;

		console.log(hotelID);
		console.log(roomNumber);
		console.log(forename);
		console.log(surname);

		if (forename == "staff1") {
			window.location.href = "stafflogin.html";
		} else if (hotelID && roomNumber && forename && surname) {
			console.log("validate");
			setValidationValues(hotelID, roomNumber, forename, surname);
		} else {
			showPopup(1);
		}
	});

	function setValidationValues(hotelID, roomNumber, forename, surname) {
		const mRoomRef = firebase.database().ref(hotelID).child("rooms").child(roomNumber).child("currentGuest");
		mRoomRef.on("value", snap => {
			if (snap.val()) {
				console.log("has guest");
				const guestKey = snap.val();
				const mGuestRef = firebase.database().ref(hotelID).child("guest").child(guestKey);
				mGuestRef.on("value", guest => {
					if (guest.child("roomNumber").val() == roomNumber &&
						guest.child("forename").val() == forename &&
						guest.child("surname").val() == surname) {
						const password = forename.toLowerCase() + surname.toLowerCase() + roomNumber;
						const email = password + "@efficiclean.com";
						console.log(password);
						console.log(email);
						const auth = firebase.auth();
						var promise = auth.signInWithEmailAndPassword(email, password);
            			promise.catch(e => console.log(e.message));
					} else {
						showPopup(2);
					}
				});
			} else {
				showPopup(2);
			}
		});
	}

	//Listens to the Firebase authentication
	firebase.auth().onAuthStateChanged(firebaseUser => {
	  if(firebaseUser) {
	    //Redirects to map page
	    console.log(firebaseUser);
	    window.location.href = "guesthome.html?hid=" + hid.value;
	  } else {
	    console.log("No user currently logged in.");
	  }
	});
});