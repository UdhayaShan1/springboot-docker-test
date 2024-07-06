async function runBatchJob() {
    try {
        const response = await fetch('/run-batch-job', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            showPopup('Batch job executed successfully.');
        } else {
            showPopup('Failed to execute batch job.');
        }
    } catch (error) {
        showPopup('An error occurred. Please try again.');
    }
}

function showPopup(message) {
    // Create the popup element
    const popup = document.createElement('div');
    popup.className = 'popup';
    popup.textContent = message;

    // Append the popup to the body
    document.body.appendChild(popup);

    // Remove the popup after 3 seconds
    setTimeout(() => {
        popup.remove();
    }, 3000);
}
