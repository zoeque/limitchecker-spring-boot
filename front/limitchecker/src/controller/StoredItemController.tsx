import axios from "axios";

const json = {
    itemName: '',
    itemType: '',
    expiredDate: ''
}

async function sendPostRequest() {
    const response = await axios.post('http://localhost:8090/create', json, {
        headers: {
            'Content-Type': 'application/json',
        }
    });
}

sendPostRequest();