$(document).ready(function() {
    fetchOrders();
});

async function deleteOrder(id) {
    try {
        const response = await fetch(`/deleteorder/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            fetchOrders();
        } else {
            window.location.href = '/home';
            errorMessageElement.textContent = await response.text();
        }
    } catch (error) {
        errorMessageElement.textContent = 'An error occurred. Please try again.';
    }
}

function fetchOrders() {
    $.ajax({
        url: '/getorders',
        method: 'GET',
        success: function(data) {
            let ordersTableBody = $('#orders-table-body');
            ordersTableBody.empty();
            data.forEach(order => {
                let row = `
                    <tr>
                        <td>${order.orderId}</td>
                        <td>${order.nameOfOrder}</td>
                        <td>${order.dateOfOrder}</td>
                        <td>${order.descriptionOfOrder}</td>
                        <td>${order.users.id}</td>
                        <td>${order.users.username}</td>
                        <td><button class="delete-btn" data-id="${order.id}">Delete</button></td>
                    </tr>
                `;
                ordersTableBody.append(row);
            });

            $('.delete-btn').click(function() {
                const id = $(this).data('id');
                deleteOrder(id);
            });
        },
        error: function(error) {
            console.log("Error fetching orders: ", error);
        }
    });
}


document.getElementById("add-orders-form").addEventListener('submit', async function (event) {
    event.preventDefault();
    const nameOfOrder = document.getElementById("orders-name").value.trim();
    const descriptionOfOrder = document.getElementById("orders-description").value.trim();
    const errorMessageElement = document.getElementById("errorMessage");
    errorMessageElement.textContent = '';

    if (!nameOfOrder) {
        errorMessageElement.textContent = "Please input name of order";
    }
    if (!descriptionOfOrder) {
        errorMessageElement.textContent = "Pleae input description of order";
    }

    try {
        const response = await fetch('/neworder', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ nameOfOrder, descriptionOfOrder})
        });

        if (response.ok) {
            fetchOrders();
            nameOfOrder.value = '';
            descriptionOfOrder.value = '';
        } else if (response.status === 401) {
            window.location.href = '/login';
        } else {
            errorMessageElement.textContent = await response.text();
        }
    } catch (error) {
        errorMessageElement.textContent = 'An error occurred. Please try again.';
    }
});