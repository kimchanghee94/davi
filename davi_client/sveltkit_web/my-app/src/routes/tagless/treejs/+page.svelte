<script>
    import { onMount } from 'svelte';
    import Tree from '@widgetjs/tree';
    import header from "../../../layout/common/header/header.svelte";

    let jsonData = {
        "body": [
            {
                "spm_row": 1,
                "SN": 1,
                "INSTT_NM": "북구시니어클럽",
                "RDNMADR": "대구광역시 북구 옥산로7길 3-1(노원동)",
                "TELNO": "053-341-4321"
            },
            {
                "spm_row": 2,
                "SN": 2,
                "INSTT_NM": "북구노인복지관",
                "RDNMADR": "대구광역시 북구 성북로 49(침산동)",
                "TELNO": "053-353-9633"
            },
            {
                "spm_row": 3,
                "SN": 3,
                "INSTT_NM": "강북노인복지관",
                "RDNMADR": "대구광역시 북구 칠곡중앙대로91길 21(관음동)",
                "TELNO": "053-313-6644"
            },
            {
                "spm_row": 4,
                "SN": 4,
                "INSTT_NM": "대불노인복지관",
                "RDNMADR": "대구광역시 북구 검단로 8-14(복현동)",
                "TELNO": "053-381-8310"
            },
            {
                "spm_row": 5,
                "SN": 5,
                "INSTT_NM": "함지노인복지관",
                "RDNMADR": "대구광역시 북구 동암로 180(구암동)",
                "TELNO": "053-326-3394"
            }
        ],
        "header": {
            "perPage": 10,
            "resultCode": "00",
            "totalRows": 5,
            "currentPage": 1,
            "resultMsg": "NORMAL_SERVICE"
        }
    };

    function makeTreeData(jsonData){
        let treeData = new Array();

        if(JSON.stringify(jsonData).includes("header")){
            console.log("jsonData contain headers");
            let headerRoot = makeTreeBasicNode();
            headerRoot["id"] = "header";
            headerRoot["text"] = "header";

            for(let hk in jsonData.header){
                let headerVal = makeTreeBasicNode();
                headerVal["id"] = hk;
                headerVal["text"] = hk + ": " + jsonData.header[hk];
                headerRoot["children"].push(headerVal);
            }

            treeData.push(headerRoot);

            let bodyRoot = new makeTreeBasicNode();
            bodyRoot["id"] = "body";
            bodyRoot["text"] = "body";
            let index = 0;

            for(let body of jsonData.body){
                for(let bk in body){
                    let bodyVal = new makeTreeBasicNode();
                    bodyVal["id"] = bk + index;
                    bodyVal["text"] = bk + ": " + body[bk];
                    bodyRoot["children"].push(bodyVal);
                }
                break;
            }
            treeData.push(bodyRoot);
        }else{
            console.log("jsonData not contain headers");
        }

        console.log("Test for treeData", treeData);
        return treeData;
    }

    function makeTreeBasicNode(){
        let node = new Object();
        node["children"] = new Array();
        // node["disabled"] = true;
        return node;
    }

    function IsJsonString(str) {
        try {
            var json = JSON.parse(str);
            return (typeof json === 'object');
        } catch (e) {
            return false;
        }
    }

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

    onMount(() => {
        const myTree = new Tree('#container', {
            data: makeTreeData(jsonData),
        });

        makeTreeData(jsonData);
    });
</script>

<div id="container"></div>
