export const csr = true;

/** @type {import('./$types').PageServerLoad} */
export async function load({fetch}){
    const apiKey = 'IDJe8lxnwi50baG6wWjnJTzAKy6TpU1JXS1TE4t7Bryl2t2qxkB3bU5CUajIQCd5';
    const data = { "title": "Where do people live?", "type": "d3-bars-stacked" };

    const postReq = await fetch('/api', {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${apiKey}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    });

    let postResp = await postReq.json();
    console.log(postResp);

    const putReqData = {
        labels: ['January', 'February', 'March', 'April', 'May'],
        values: [15, 10, 12, 8, 20]
    };

   /* const putReq = await fetch('/put/' + postResp.id, {
        method: 'PUT',
        headers: {
            Authorization: `Bearer ${apiKey}`,
            'Content-Type': 'text/csv',
        },
        body : JSON.stringify(putReqData)
        // body: 'country;Share of population that lives in the capital;in other urban areas;in rural areas' +
        //     '        Iceland (Reykjavík);56.02;38;6' +
        //     '        Argentina (Buenos Aires);34.95;56.6;8.4' +
        //     '    Japan (Tokyo);29.52;63.5;7' +
        //     '    UK (London);22.7;59.6;17.7' +
        //     '    Denmark (Copenhagen);22.16;65.3;12.5' +
        //     '    France (Paris);16.77;62.5;20.7' +
        //     '    Russia (Moscow);8.39;65.5;26.1' +
        //     '    Niger (Niamey);5.53;12.9;81.5' +
        //     '    Germany (Berlin);4.35;70.7;24.9' +
        //     '    India (Delhi);1.93;30.4;67.6' +
        //     '    USA (Washington, D.C.);1.54;79.9;18.6' +
        //     '    China (Beijing);1.4;53;45.6'
    });*/

   // let putResp = await  putReq.json();


    console.log(postResp);
    return postResp;
}