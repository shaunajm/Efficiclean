$(document).ready(function () {

  //Function to display popup
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

  //Access the SVG map object
  var map = document.getElementById("map");
  var mapDoc = map.contentDocument;

  //Access the hotel ID from the query string URL
  const hotelID = window.location.href.split("=").pop();

  var mRoomRef;

  if (hotelID) {
    //Reference to the rooms branch of the hotel
    mRoomRef = firebase.database().ref().child(hotelID).child("rooms");

    //Value event listener that iterates through the room objects
    mRoomRef.on("value", snap => {
      snap.forEach(room => {
        //Getting the relevant information from the room object
        var status = room.child("status").val();
        var roomNumber = room.key;

        //Referencing the correct vector path
        var mapKey = "pRoom" + roomNumber;
        var mapItem = mapDoc.getElementById(mapKey);

        //Changing vector path colour based on its status
        if (status == "Waiting") {
          mapItem.setAttribute("fill", "#1589FF");
        } else if (status == "Do Not Disturb") {
          mapItem.setAttribute("fill", "#000000");
        } else if (status == "To Be Cleaned") {
          mapItem.setAttribute("fill", "#800000");
        } else if (status == "Completed") {
          mapItem.setAttribute("fill", "#008000");
        } else if (status == "In Process") {
          mapItem.setAttribute("fill", "#ff6600");
        } else {
          mapItem.setAttribute("fill", "#ffeeaa");
        }

      });
    });
  }

  //Reference other html elements
  const rnum = document.getElementById("rnum");
  const markRoom = document.getElementById("mark-room-btn");
  const logout = document.getElementById("logout-btn");

  //Event listener for mark room button
  markRoom.addEventListener("click", e => {
    var roomNumber = rnum.value;

    //Reference to jobs branch in database
    const mJobRef = firebase.database().ref().child(hotelID).child("jobs");
    if (roomNumber) {
      //Reference to inputted room
      mRoomRef.child(roomNumber).once("value", snap => {
        //Validate that room number exists
        if (snap.val()) {
          //Change room status and create a job
          mRoomRef.child(roomNumber).set({status: "To Be Cleaned"});
          mJobRef.push().set({
            priority: 0,
            roomNumber: roomNumber.toString(),
            status: false
          })
          showPopup(1);
        } else {
          showPopup(2);
        }
      });
    } else {
      showPopup(2);
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
    } else {
      console.log("No user currently logged in.");
      history.back();
    }
  });

});
