document.addEventListener("DOMContentLoaded", function() {
    fetchUserDetails();
});

async function fetchUserDetails() {
    try {
        const response = await fetch(`/user/display`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const user = await response.json();
            displayUserDetails(user);
        } else if (response.status === 404) {
            displayErrorMessage("User not found");
        } else {
            displayErrorMessage("Error fetching user details. Please try again.");
        }
    } catch (error) {
        displayErrorMessage("An error occurred. Please try again.");
    }
}

function displayUserDetails(user) {
    document.getElementById("user-name").textContent = user.username;
    document.getElementById("user-email").textContent = user.email;

    document.getElementById("user-details").style.display = "block";
    document.getElementById("error-message").textContent = "";

    document.getElementById("edit-user-button").addEventListener("click", function() {
        showEditForm(user);
    });
}

function showEditForm(user) {
    document.getElementById("edit-name").value = user.username;
    document.getElementById("edit-email").value = user.email;
    document.getElementById("edit-user-form").style.display = "block";
}

document.getElementById("edit-form").addEventListener("submit", async function(event) {
    event.preventDefault();
    const username = document.getElementById("edit-name").value.trim();
    const email = document.getElementById("edit-email").value.trim();
    if (!username && !email) {
        displayErrorMessage("Some field should be changed fanta lite bruv");
        return;
    }

    try {
        const response = await fetch('/user/update', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({username, email})
        });

        if (response.ok) {
            fetchUserDetails(); // Refresh user details
            document.getElementById("edit-user-form").style.display = "none";
        } else {
            displayErrorMessage("Error updating user details. Please try again.");
        }
    } catch (error) {
        displayErrorMessage("An error occurred. Please try again.");
    }
});

document.getElementById("edit-password-form").addEventListener("submit", async function(event) {
    event.preventDefault();
    const password = document.getElementById("password").value.trim();
    const confirmPassword = document.getElementById("confirm-password").value.trim();

    if (password !== confirmPassword) {
        displayErrorMessage("Mismatch in password!");
        return;
    }

    if (!validatePassword(password)) {
        displayErrorMessage("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
        return;
    }



    try {
        const response = await fetch('/user/updatepassword', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({password})
        });

        if (response.ok) {
            window.location.href = '/login';
        } else {
            displayErrorMessage("Error updating password. Please try again.");
        }
    } catch (error) {
        displayErrorMessage("An error occurred. Please try again.");
    }
});

function validatePassword(password) {
    const minLength = 8;
    const hasUppercase = /[A-Z]/.test(password);
    const hasLowercase = /[a-z]/.test(password);
    const hasNumber = /\d/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    return password.length >= minLength && hasUppercase && hasLowercase && hasNumber && hasSpecialChar;
}
function displayErrorMessage(message) {
    document.getElementById("error-message").textContent = message;
}
