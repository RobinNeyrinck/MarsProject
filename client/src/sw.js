const _title = "MarSolutions";

self.addEventListener("push", e => { // NOSONAR
    const msg = e.data.text();
    self.registration.showNotification(_title, {
        body: msg
    });
});
