document.addEventListener("DOMContentLoaded", function() {
    fetchOrderDetails();
});

async function fetchOrderDetails() {
    try {
        const response = await fetch('/edit/displayorder', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const order = await response.json();
            document.getElementById('order-name').value = order.nameOfOrder;
            document.getElementById('order-date').value = order.dateOfOrder;
            document.getElementById('order-description').value = order.descriptionOfOrder;
        } else if (response.status === 401) {
            window.location.href = '/login';
        } else {
            document.getElementById('errorMessage').textContent = 'Error fetching order details. Please try again.';
        }
    } catch (error) {
        document.getElementById('errorMessage').textContent = 'An error occurred. Please try again.';
    }
}

document.getElementById("edit-order-form").addEventListener('submit', async function(event) {
    event.preventDefault();
    const nameOfOrder = document.getElementById("order-name").value.trim();
    const dateOfOrder = document.getElementById("order-date").value.trim();
    const descriptionOfOrder = document.getElementById("order-description").value.trim();
    const errorMessageElement = document.getElementById("errorMessage");


    if (!nameOfOrder && !dateOfOrder && !descriptionOfOrder) {
        errorMessageElement.textContent = "At least one field should be changed";
        return;
    }

    if (dateOfOrder && !isValidDate(dateOfOrder)) {
        errorMessageElement.textContent = "Date is invalid format, YYYY-MM-DD";
        return;
    }

    try {
        const response = await fetch('/edit/updateorder', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ nameOfOrder, dateOfOrder, descriptionOfOrder })
        });

        if (response.ok) {
            errorMessageElement.textContent = 'Success! Redirecting..';
            window.location.href = '/order'; // Redirect to orders page or any other appropriate page
        } else if (response.status === 401) {
            window.location.href = '/login';
        } else {
            errorMessageElement.textContent = await response.text();
        }
    } catch (error) {
        errorMessageElement.textContent = 'An error occurred. Please try again.';
    }
});

function isValidDate(dateString) {
    const regex = /^\d{4}-\d{2}-\d{2}$/;
    if (!dateString.match(regex)) return false;

    const date = new Date(dateString);
    const timestamp = date.getTime();

    if (typeof timestamp !== 'number' || Number.isNaN(timestamp)) return false;

    return dateString === date.toISOString().split('T')[0];
}