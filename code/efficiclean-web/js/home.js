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

  	//Reference HTML elements
	const logout = document.getElementById("logout-btn");

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
      } else {
        console.log("No user currently logged in.");
        history.back();
      }
    });
});