import React, { useState } from 'react';
import './components.css'

const CreateButton: React.FC = () => {

    const [count, setCount] = useState(0);
    return (
        <div className="card">
            <button onClick={() => setCount((count) => count + 1)}>
                登録 {count}
            </button>
        </div>
    );
}
export default CreateButton;
