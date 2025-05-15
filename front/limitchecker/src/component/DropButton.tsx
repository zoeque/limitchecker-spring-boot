import './components.css'
import '../controller/StoredItemDropController'

interface DropButtonProps {
    storedItemIdentifier: Int16Array;
    itemName: string;
    itemType: string;
    expiredDate: string;
    sendDropRequest: (
        storedItemIdentifier: Int16Array,
        itemName: string,
        itemType: string,
        expiredDate: string
    ) => void;
    // function to set the message in the parent component
    setMessage: (message: string) => void; 
}

function DropButton({
    storedItemIdentifier,
    itemName,
    itemType,
    expiredDate,
    sendDropRequest,
    setMessage,
}: DropButtonProps) {
    function clickButton() {
        try {
            // send the drop request to the controller
            sendDropRequest(storedItemIdentifier, itemName, itemType, expiredDate);
            // set the message to indicate success
            setMessage(`${itemName}を削除しました。`); 
        } catch (e) {
            // handle error and set the message
            setMessage(`予期せぬエラーが発生しました。エラー：${e}`); 
        }
    }

    return (
        <div className="dropbutton">
            <button onClick={clickButton}>削除</button>
        </div>
    );
}

export default DropButton;