document.getElementById('registerForm').addEventListener('submit', async function (event) {
    event.preventDefault();
    const username = document.getElementById('username').value.trim().toLowerCase();
    const password = document.getElementById('password').value.trim();
    const email = document.getElementById('email').value.trim().toLowerCase();
    const errorMessageElement = document.getElementById('errorMessage');

    // Clear previous error message
    errorMessageElement.textContent = '';

    // Validation checks
    if (!username) {
        errorMessageElement.textContent = 'Username is required.';
        return;
    }

    if (!password) {
        errorMessageElement.textContent = 'Password is required.';
        return;
    }

    if (password.length < 8) {
        errorMessageElement.textContent = 'Password must be at least 8 characters long.';
        return;
    }

    if (!email) {
        errorMessageElement.textContent = 'Email is required.';
        return;
    }

    try {
        const response = await fetch('/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password, email })
        });

        if (response.ok) {
            window.location.href = '/login';
        } else {
            errorMessageElement.textContent = await response.text();
        }
    } catch (error) {
        errorMessageElement.textContent = 'An error occurred. Please try again.';
    }
});
