<script>
    import { onMount} from "svelte";
    import JSONEditor from 'jsoneditor';
    import 'jsoneditor/dist/jsoneditor.css';

    const treeData = [
        {
            id: '1',
            text: 'node-1',
            children: [{id: '1-0', text: 'node-1-0'}, {id: '1-1', text: 'node-1-1'}],
        },
        {
            text: 'node-0',
            children: [
                {
                    id: '0-0',
                    text: 'node-0-0',
                    children: [
                        {id: '0-0-0', text: 'node-0-0-0'},
                        {id: '0-0-1', text: 'node-0-0-1'},
                        {id: '0-0-2', text: 'node-0-0-2'},
                    ],
                },
                {id: '0-1', text: 'node-0-1'},
            ],
        },
    ];

    let jsonData = {
        header : {
            resultMsg : "success",
            resultCode : "000",
        },
        body : [
            {
                company : "kenny",
                group : "new system operation"
            },
            {
                home : "seoul",
                phone : "111-222-3333",
                teamCnt : 100
            }
        ],

    };

    let container;
    let editor;
    let jsonStr = JSON.stringify(jsonData, null, 2);

    // Create the JSONEditor instance when the component is mounted
    onMount(() => {
        const options = {
            mode: 'tree', // or 'code' for raw text editor
            onChangeJSON: (json) => {
                jsonStr = JSON.stringify(json, null, 2);
            },
        };
        editor = new JSONEditor(container, options);
        editor.set(jsonData);
    });
</script>

<style>
    /* Style the container element */
    .jsoneditor-container {
        height: 400px;
    }
</style>

<div>
    <div class="jsoneditor-container" bind:this={container}></div>
</div>