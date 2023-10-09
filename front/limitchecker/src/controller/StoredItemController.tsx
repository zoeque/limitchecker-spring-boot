import axios from "axios";

export const sendPostRequest = async (itemName: string, itemType: string, expiredDate: string) => {

    try {
         await axios.post('http://localhost:8080/create', {
            itemName: itemName,
            itemType: itemType,
            expiredDate: expiredDate
        });
    } catch (error) {
        console.warn(error);
    }
}

