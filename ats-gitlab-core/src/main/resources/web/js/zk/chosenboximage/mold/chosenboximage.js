function (out) {
    out.push('<select  id="', this.uuid, '-chosenbox-image" multiple >' + this.domContent_() + '</select>');
}