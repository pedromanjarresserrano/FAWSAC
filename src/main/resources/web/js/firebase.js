importScripts('https://www.gstatic.com/firebasejs/4.8.1/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/4.8.1/firebase-messaging.js');

var config = {
    apiKey: "", authDomain: "", databaseURL: "", projectId: "",
    storageBucket: "",
    messagingSenderId: ""
};
firebase.initializeApp(config);
const messaging = firebase.messaging();
navigator.serviceWorker.register('firebase-messaging-sw')
    .then((registration) = > {
    if( 'undefined' !== typeof messaging.b)
delete(messaging.b);
messaging.useServiceWorker(registration);
});
;messaging.requestPermission().then(function () {
    console.log('have permition');
    return messaging.getToken();
}).then(function (token) {
    payload = token;
    zAu.send(new zk.Event(zk.Widget.$(this), 'onUser', payload));
    console.log(token);
}).catch(function (err) {
    console.log('Error Occured.');
    console.log(err);
});
messaging.onMessage(function (payload) {
    console.log('onMessage', payload);
});