import React, { useState } from 'react';
import './components.css'
import '../App.css'

interface Option {
    label: string;
    value: string;
}

type PulldownProps = {
    onRowSelect: (value: string) => void;
};

function Pulldown({ onRowSelect }: PulldownProps) {
    const [selectedRow, setRow] = useState<Option | null>(null);

    // The dropdown values used in ItemType
    const options: Option[] = [
        { value: 'spice', label: '調味料' },
        { value: 'egg', label: '卵' },
        { value: 'meat', label: '肉類' },
        { value: 'juice', label: '飲料' },
        { value: 'dairy', label: '乳製品' },
        { value: 'snack', label: '菓子類' },
        { value: 'others', label: 'その他' },
    ];

    /**
     * The function to select the value from Pulldown list.
     * 
     * @param event The event to change the row in a pulldown.
     */
    const handlePulldown = (event: React.ChangeEvent<HTMLSelectElement>) => {
        const selectedValue = event.target.value;
        const selected = options.find((option) => option.value === selectedValue);
        setRow(selected || null);
        onRowSelect(selectedValue);
    };


    return (
        <div className='item-type-pulldown'>
            <select value={selectedRow?.value || 'others'} onChange={handlePulldown}>
                {options.map((row) => (
                    <option key={row.value} value={row.value}>
                        {row.label}
                    </option>
                ))}
            </select>
        </div>
    );
};
export default Pulldown;