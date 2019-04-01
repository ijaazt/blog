import React, {Component} from 'react';

class Article extends Component {
    constructor(props) {
        super(props);
    }
    render() {
        const {onClick, article} = this.props;
        const {addedAt, title, author, headline, content} = article;

        return (
        <section className="article" >
            <button onClick={onClick}>Go Back</button>
            <Header addedAt={addedAt} title={title} firstName={author.firstname}/>
            <Description headline={headline} content={content}/>
        </section>
        )
    }
}

function Header (props) {
    const {addedAt, title, firstName} = props;
    return (
        <header className="article-header">
            <h1 className="article-title">{title}</h1>
            <p className="article-meta">By <strong>{firstName}</strong>, on <strong>{addedAt}</strong>
            </p>
        </header>
    )
}
function Description({headline, content}) {
    return (
        <div id={"article-description"}>
            <p>{headline}</p>
            <p>{content}</p>
        </div>
    )
}
export default Article;

