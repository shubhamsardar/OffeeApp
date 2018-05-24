
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.neworder = functions.database.ref('/orders/{pushId}')
    .onCreate((snap, context) => {
      // Grab the current value of what was written to the Realtime Database.
      var newValue = snap.val();

	  // Create a DATA notification
    const payload = {
       data: {
        type: "1",
        mUserName: newValue.name,
      }
    };
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };


         return admin.messaging().sendToTopic(newValue.hpass, payload, options);


    });
	
	exports.orderDelivered = functions.database.ref('/history/{pushId}')
    .onCreate((snap, context) => {
      // Grab the current value of what was written to the Realtime Database.
      var newValue = snap.val();

	  // Create a DATA notification
    const payload = {
       data: {
        type: "2",
        orderCost: ""+newValue.orderCost,
      }
    };
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };


         return admin.messaging().sendToTopic(newValue.mUserUid, payload, options);


    });