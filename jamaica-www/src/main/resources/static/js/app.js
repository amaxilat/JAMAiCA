function enableClassification(id, state) {
    $.post("/web/classification/" + id + "/enable/" + state).success(function (e) {
        window.location = "/web/classification/" + id;
    });
}

function trainClassification(id) {
    $.post("/web/classification/" + id + "/train").success(function (e) {
        window.location = "/web/classification/" + id;
    });
}

function trainClassification(id) {
    $.post("/web/classification/" + id + "/subscribe").success(function (e) {
        window.location = "/web/classification/" + id;
    });
}

function deleteClassification(id) {
    $.post("/web/classification/" + id + "/delete").success(function (e) {
        window.location = "/web/classification/";
    });
}

function getTagDomains() {
    $.get("https://annotations.organicity.eu/tagDomains").success(function (e) {
        var ids = $.map(e, function (a, i) {
            if (a.tags.length > 1) {
                $('#tags').append($('<option>', {
                    value: a.urn,
                    text: a.description
                }));
            }
        });
        updateTags();
    });
}

function updateTags() {
    $("#tags-pre").text("");
    $.get("https://annotations.organicity.eu/tagDomains/" + $("#tags").val()).success(function (e) {
        var ids = $.map(e.tags, function (a, i) {
            return a.urn;
        });
        $("#tags-pre").text(JSON.stringify(ids, null, 1));
    });
}

function updateEntities() {
    $("#entities-pre").text("");
    $.get("https://discovery.organicity.eu/v0/assets?per=5&type=" + $("#typePat").val()).success(function (e) {
        var ids = $.map(e, function (a, i) {
            return a.id;
        });
        $("#entities-pre").text(JSON.stringify(ids, null, 1));
    });
}
