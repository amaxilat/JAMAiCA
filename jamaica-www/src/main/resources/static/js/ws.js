var stompClient = null;

//add a new style 'foo'
$.notify.addStyle('foo', {
    html:
    "<div class='notifyjs-bootstrap-base notifyjs-bootstrap-success'>" +
    "<b>New Classification (job <span class='classificationConfig' data-notify-html='classificationConfig'/>)</b><br/>" +
    "<a class='linkme' data-notify-text='button'>" +
    "AssetUrn: <span class='assetUrn' data-notify-html='assetUrn'/><br/>" +
    "Tag: <span class='tagUrn' data-notify-html='tagUrn'/><br/>" +
    "<span class='classificationConfig hidden' data-notify-html='classificationConfig'/>" +
    "</a>" +
    "</div>"
});

//listen for click events from this style
$(document).on('click', '.notifyjs-foo-base .linkme', function () {
    window.location = "/web/classification/" + $(this).find(".classificationConfig").text()+ "/results";
});

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/classification', function (message) {
            showGreeting(JSON.parse(message.body));
        });
    });
}
$(document).ready(function () {
    connect();
});

// $(function () {
//     $("form").on('submit', function (e) {
//         e.preventDefault();
//     });
//     $( "#connect" ).click(function() {  });
//     $( "#disconnect" ).click(function() { disconnect(); });
//     $( "#send" ).click(function() { sendName(); });
// });