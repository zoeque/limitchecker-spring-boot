import axios from "axios";

export const sendDropRequest = async (storedItemIdentifier: Int16Array, itemName: string, itemType: string, expiredDate: string) => {

    try {
         await axios.post('http://localhost:8080/drop', {
            storedItemIdentifier: storedItemIdentifier,
            itemName: itemName,
            itemType: itemType,
            expiredDate: expiredDate
        });
    } catch (error) {
        console.warn(error);
    }
}
