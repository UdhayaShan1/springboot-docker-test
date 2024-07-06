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
    event.preventDefault();// Reuse the user ID
    const username = document.getElementById("edit-name").value.trim();
    const email = document.getElementById("edit-email").value.trim();
    if (!username && !email) {
        displayErrorMessage("Some field should be changed fanta lite bruv");
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

function displayErrorMessage(message) {
    document.getElementById("error-message").textContent = message;
    document.getElementById("user-details").style.display = "none";
}