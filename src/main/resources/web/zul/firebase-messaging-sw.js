// Give the service worker access to Firebase Messaging.
// Note that you can only use Firebase Messaging here, other Firebase libraries
// are not available in the service worker.
importScripts('https://www.gstatic.com/firebasejs/5.4.1/firebase.js');
importScripts('https://www.gstatic.com/firebasejs/5.4.1/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/5.4.1/firebase-messaging.js');

// Initialize the Firebase app in the service worker by passing in the
// messagingSenderId.
var config = {
    apiKey: "AIzaSyAzrhhTFHjpeBIpgLIZu2O9MLSlng2lTpw",
    authDomain: "alertsystem-5813d.firebaseapp.com",
    databaseURL: "https://alertsystem-5813d.firebaseio.com",
    storageBucket: "alertsystem-5813d.appspot.com",
    messagingSenderId: "162364664738"
};
firebase.initializeApp(config);

// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.
const messaging = firebase.messaging();
