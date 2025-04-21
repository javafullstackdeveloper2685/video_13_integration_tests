document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('#messageForm');
    const output = document.querySelector('#output');

    form.addEventListener('submit', (e) => {
        e.preventDefault();

        const formData = new FormData(form);
        const dataToSend = Object.fromEntries(formData.entries());

        fetch('/game', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dataToSend),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            output.innerHTML = `<p>Status: ${data.status}</p><p>Message: ${data.echoMessage}</p>`;
        })
        .catch((error) => {
            output.innerHTML = `<p>Error: ${error.message}</p>`;
        });
    });
});