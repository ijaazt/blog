import React, {Component} from 'react';
import Preview from './Preview';
class Blog extends Component {
    render() {
        const {articles, onClick, title} = this.props
        const preview = articles.map(content => <Preview
            content={content} key={content.id} onClick={e => { onClick(content.id) }}
        />)
        return (
            <div className={"Blog"}>
                <h1>{title}</h1>
                {preview}
            </div>
        )
    }
}
export default Blog
